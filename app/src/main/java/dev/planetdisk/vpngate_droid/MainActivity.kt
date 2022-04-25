package dev.planetdisk.vpngate_droid

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.AppLaunchChecker
import androidx.core.content.edit
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var mAdapter: CustomAdapter
    private lateinit var mServerList: ArrayList<ServerList>
    private lateinit var prefSetting: SharedPreferences
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val uri = it.data?.data
        contentResolver.takePersistableUriPermission(
            uri?:return@registerForActivityResult,
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )
        prefSetting.edit {
            putString("uri", uri.toString())
            AppLaunchChecker.onActivityCreate(this@MainActivity)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) = runBlocking{
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prefSetting = PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
        lateinit var vpnGateCache: String
        val cacheServerList: ArrayList<ServerList> = arrayListOf()
        if(checkConnectNetwork()){
            //初回起動・権限要求
            if(!AppLaunchChecker.hasStartedFromLauncher(this@MainActivity)){
                MaterialAlertDialogBuilder(this@MainActivity)
                    .setTitle("VPNGate-droid would like to Access Your Folder.")
                    .setMessage("This permission is only used to save OpenVPN profiles.\nIf you allow, the app be able to access only one folder you specify.")
                    .setNeutralButton("Cancel") { _, _ ->
                        finish()
                    }
                    .setNegativeButton("Decline") { _, _ ->
                        finish()
                    }
                    .setPositiveButton("Accept") { _, _ ->
                        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
                        launcher.launch(intent)
                    }
                    .setOnCancelListener {
                        finish()
                    }
                    .show()
            }
            vpnGateCache = withContext(Dispatchers.Default) {
                VpnGateCsvApi.retrofitService.getCsv()
            }
            val serverList = VpnGateCsvApi.parseCsv(vpnGateCache)
            serverList.forEach{
                cacheServerList += ServerList(it[0],it[1],it[3],it[5],it[12],it[14])
            }
        }else{
            MaterialAlertDialogBuilder(this@MainActivity)
                .setTitle("Network is unavailable.")
                .setMessage("Please connect to a network.\nPerhaps network administrator is blocking access to \"vpngate.net\".\nIn that case, please connect to a libre network and reopen this application.")
                .setPositiveButton("Accept") { _, _ ->
                    finish()
                }
                .setOnCancelListener {
                    finish()
                }
                .show()
        }
        mServerList = cacheServerList

        // RecyclerViewの取得
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)

        // LayoutManagerの設定
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

        // CustomAdapterの生成と設定
        mAdapter = CustomAdapter(mServerList)
        recyclerView.adapter = mAdapter

        mAdapter.setOnButtonClickListener(
            object: CustomAdapter.OnButtonClickListener{
                override fun onButtonClick(server: ServerList) {
                    saveFile(server)
                }
            }
        )
    }
    private fun saveFile(server:ServerList) {
        val uri = prefSetting.getString("uri", "")?.toUri() ?: return
        DocumentFile.fromTreeUri(this, uri)?.apply {
            val textFile = if (findFile(server.hostName+".ovpn")?.exists() == true) {
                findFile(server.hostName+".ovpn") ?: return@apply
            } else {
                createFile("application/x-openvpn-profile", server.hostName+".ovpn") ?: return@apply
            }
            contentResolver.openOutputStream(textFile.uri)?.apply {
                write(Base64.getDecoder().decode(server.openVpnConfig.toByteArray()).toString(Charsets.UTF_8).toByteArray())
                close()
            }
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(textFile.uri, "application/x-openvpn-profile")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
            try{
                startActivity(intent)
            }catch (e: android.content.ActivityNotFoundException) {
                MaterialAlertDialogBuilder(this@MainActivity)
                    .setTitle("OpenVPN is not installed.")
                    .setMessage("This application required \"OpenVPN Connect\".\nIf you never installed, please install it.")
                    .setPositiveButton("Accept") { _, _ ->
                        val webpage: Uri =
                            Uri.parse("https://play.google.com/store/apps/details?id=net.openvpn.openvpn")
                        val ovpnIntent = Intent(Intent.ACTION_VIEW, webpage)
                        if (ovpnIntent.resolveActivity(packageManager) != null) {
                            startActivity(ovpnIntent)
                        }
                    }
                    .setOnCancelListener {
                        finish()
                    }
                    .show()
            }
        }
    }
    private fun checkConnectNetwork(): Boolean{
        var result = true
        runBlocking {
            try{
                VpnGateCsvApi.pingService.getPing()
            }catch(e: Exception){
                result = false
            }
        }
        return result
    }
}
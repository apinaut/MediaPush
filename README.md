MediaPush
=========

This one is a little demo project how the API O MAT backend as a service can be used to build a media push in android.


Installation
------------

Pull the content of this repository to your dev environment and create an android project from it. Then, please, create your
own accout at http://apiomat.com and do the following:
* Create an app
* Add at least the Chat module
* Deploy on live system 
* Download the SDK and unzip it into the /src folder. It is OK to overwrite the existing files.

You can also use the exiting files, but all requests will access one of our testing systems, and may not work every time. 

Media Push
------------

Attach images and files on your push messages and send them along now.

```java
PushMessage pushMessage = new PushMessage();
pushMessage.setPayload(„Write your message here“);
pushMessage.setReceiverUserName(„nameOfRecipient“);
```

Now attach an image and send the push to the recipient:

```java
pushMessage.postImageAsync(byteArray, new AOMEmptyCallback() {
   public void isDone(ApiomatRequestException exception) {
        // If the image is uploaded,
        // the push message must be 
        // sent to the recipient.
        pushMessage.sendAsync(new AOMEmptyCallback() {
 
            @Override
            public void isDone(ApiomatRequestException exception) {
                // The Push message has been sent
            }});
 
    }});
```

..or attach a file and send the message to the recipient:

```java
pushMessage.postFileAsync(byteArray, new AOMEmptyCallback() {
   public void isDone(ApiomatRequestException exception) {
        // If the file is uploaded,
        // the push message must be 
        // sent to the recipient.
        pushMessage.sendAsync(new AOMEmptyCallback() {
 
            @Override
            public void isDone(ApiomatRequestException exception) {
                // The Push message has been sent
            }});
 
    }});
```


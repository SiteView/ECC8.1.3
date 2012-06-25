keytool   -genkey   -keystore   fisheye.keystore   -alias   fisheye   -validity 3650
jarsigner.exe -keystore   fisheye.keystore   fisheye.jar   fisheye   
keytool   -export   -keystore   fisheye.keystore   -alias   fisheye   -file   fisheye.cer
# SmartHelper_MiBand
Aplicación Android que utiliza los servicios de la MiBand 2 para medir el ritmo cardíaco automáticamente.

Para poder sincronizar la aplicación es necesario tener vinculada la miBand con la cuenta oficial de Xiaomi. 
Es una aplicación muy sencilla que se compone de dos pantallas: Principal y Medición.

1) Principal. El usuario deberá introducir la dirección Bluetooth de la MiBand (puede consultarse en la app MiFit en las opciones de la pulsera). A continuación, deberá comprobar la conexión a través del botón "Try Connection". Si es exitosa, se activará el botón "Connect". Al pulsarlo, accederíamos a la siguiente pantalla.

2) Medicion. A través de un Intent, recibiremos la dirección bluetooth de la pulsera y obtendremos un objeto de tipo BluetoothDevice con las características de la pulsera. Una vez ahí, a través de un hilo, llamaremos al servicio correspondiente a la medición del ritmo cardíaco, y cada 20 segundos tomaremos dicha medida y la mostraremos en la app. Una vez obtenida la medición, será enviada automáticamente al servidor, a través de una petición POST del protocolo HTTP.  

El último paso, consiste en enviar dichas mediciones a un servidor donde podrán ser procesadas. 

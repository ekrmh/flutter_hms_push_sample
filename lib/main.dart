import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(),
    );
  }
}

class MyHomePage extends StatefulWidget {
  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  static const platform =
      const MethodChannel('com.ekrmh.hmspushsample/notification');

  String notificationData;
  String token;

  Future<dynamic> _handleNotification(MethodCall call) async {
    switch (call.method) {
      case "notification":
        setState(() {
          notificationData = call.arguments;
        });
        break;
      case "token":
        setState(() {
          token = call.arguments;
        });
        break;
    }
  }

  void _getToken() {
    platform.invokeMethod("token");
  }

  @override
  void initState() {
    super.initState();
    platform.setMethodCallHandler(_handleNotification);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("HMS Push Sample"),
      ),
      body: Column(
        children: <Widget>[
          Padding(
            padding: EdgeInsets.all(16),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Container(
                  padding: const EdgeInsets.only(bottom: 16),
                  child: Text(
                    'Token\n\n${token ?? "Press FAB for get token."}',
                    style: TextStyle(
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
                Divider(
                  color: Colors.blue,
                  thickness: 2.0,
                ),
                Container(
                    padding: EdgeInsets.only(top: 16),
                    child: Text(
                      'Data\n\n${notificationData ?? "No notification has been received yet."}',
                      style: TextStyle(
                        color: Colors.grey[500],
                      ),
                    )),
              ],
            ),
          ),
        ],
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _getToken,
        tooltip: 'Get Token',
        child: Icon(Icons.notifications),
      ),
    );
  }
}

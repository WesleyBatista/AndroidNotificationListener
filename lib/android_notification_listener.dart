import 'dart:async';

import 'package:flutter/services.dart';


import 'dart:io' show Platform;

/// Custom Exception for the plugin,
/// thrown whenever the plugin is used on platforms other than Android
class NotificationException implements Exception {
  String _cause;

  NotificationException(this._cause);

  @override
  String toString() {
    return _cause;
  }
}

class NotificationEvent {
  String packageMessage;
  String packageName;
  String packageExtra;
  String packageText;
  DateTime timeStamp;

  NotificationEvent({this.packageName, this.packageMessage, this.timeStamp , this.packageExtra , this.packageText});

  factory NotificationEvent.fromMap(Map<dynamic, dynamic> map) {
    DateTime time = DateTime.now();
    String name = map['packageName'];
    String message = map['packageMessage'];
    String text = map['packageText'];
    String extra =  map['packageExtra'];

    return NotificationEvent(packageName: name, packageMessage: message, timeStamp: time,packageText: text , packageExtra: extra);
  }

  @override
  String toString() {
    return "Notification Event \n Package Name: $packageName \n - Timestamp: $timeStamp \n - Package Message: $packageMessage";
  }
}

NotificationEvent _notificationEvent(dynamic data) {
  return new NotificationEvent.fromMap(data);
}

class AndroidNotificationListener {
  static const EventChannel _notificationEventChannel =
  EventChannel('notifications.eventChannel');

  Stream<NotificationEvent> _notificationStream;

  Stream<NotificationEvent> get notificationStream {
    if (Platform.isAndroid) {

      if (_notificationStream == null) {
        _notificationStream = _notificationEventChannel
            .receiveBroadcastStream()
            .map((event) => _notificationEvent(event));
      }
      return _notificationStream;
    }
    throw NotificationException(
        'Notification API exclusively available on Android!');
  }
}

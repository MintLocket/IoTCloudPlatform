#include <ArduinoBearSSL.h>
#include <ArduinoECCX08.h>
#include <ArduinoMqttClient.h>
#include <WiFiNINA.h> // change to #include <WiFi101.h> for MKR1000
#include "arduino_secrets.h"
#define LIGHT_PIN 2     // Digital pin connected to the light sensor
#define piezoPin 6
#define C_4 261  //도
#define D_5 587  //레
#define E_4 329  //미
#define F_4 349  //파
#define G_4 392  //솔
#define A_4 440  //라
#define c_4 261  //시
#define D_4 293  //도

#include <ArduinoJson.h>
#include "Buzzer.h"

/////// Enter your sensitive data in arduino_secrets.h
const char ssid[]        = SECRET_SSID;
const char pass[]        = SECRET_PASS;
const char broker[]      = SECRET_BROKER;
const char* certificate  = SECRET_CERTIFICATE;

WiFiClient    wifiClient;            // Used for the TCP socket connection
BearSSLClient sslClient(wifiClient); // Used for SSL/TLS connection, integrates with ECC508
MqttClient    mqttClient(sslClient);

unsigned long lastMillis = 0;
int SoundSensor = A0;
int sound_value = 0;
int light_value = 0;

Buzzer buzzer(piezoPin); //부저 객체 생성, 핀 세팅

void setup() {
  Serial.begin(115200);
  while (!Serial);

  if (!ECCX08.begin()) {
    Serial.println("No ECCX08 present!");
    while (1);
  }

  // Set a callback to get the current time
  // used to validate the servers certificate
  ArduinoBearSSL.onGetTime(getTime);

  // Set the ECCX08 slot to use for the private key
  // and the accompanying public certificate for it
  sslClient.setEccSlot(0, certificate);

  // Optional, set the client id used for MQTT,
  // each device that is connected to the broker
  // must have a unique client id. The MQTTClient will generate
  // a client id for you based on the millis() value if not set
  //
  // mqttClient.setId("clientId");

  // Set the message callback, this function is
  // called when the MQTTClient receives a message
  mqttClient.onMessage(onMessageReceived);
  //사운드 센서 셋팅
  // pinMode(piezoPin, OUTPUT);
}

void loop() {
  if (WiFi.status() != WL_CONNECTED) {
    connectWiFi();
  }

  if (!mqttClient.connected()) {
    // MQTT client is disconnected, connect
    connectMQTT();
  }

  // poll for new MQTT messages and send keep alives
  mqttClient.poll();
  light_value = (analogRead(LIGHT_PIN)/10);
  if(light_value > 30) //조도 센서 값이 30 이상이면, 사이렌 출력 
  {
    buzzer.tone(A_4);    // 라 음계로 소리를 내기 시작
    delay(1000);          // 1초 동안 지속
    
    buzzer.tone(D_5); // 레 음계로 소리를 내기 시작
    delay(1000);            // 1초 동안 지속
  }

  // publish a message roughly every 5 seconds.
  if (millis() - lastMillis > 2000) { //2초마다 메시지를 전송 -> 값 다르면 db에 저장 안함
    lastMillis = millis();
    char payload[512];
    getDeviceStatus(payload); //디바이스 상태 정보를 payload에 담아 주는 함수 호출
    sendMessage(payload);
    delay(10);
  }
  
}

unsigned long getTime() {
  // get the current time from the WiFi module  
  return WiFi.getTime();
}

void connectWiFi() {
  Serial.print("Attempting to connect to SSID: ");
  Serial.print(ssid);
  Serial.print(" ");

  while (WiFi.begin(ssid, pass) != WL_CONNECTED) {
    // failed, retry
    Serial.print(".");
    delay(5000);
  }
  Serial.println();

  Serial.println("You're connected to the network");
  Serial.println();
}

void connectMQTT() {
  Serial.print("Attempting to MQTT broker: ");
  Serial.print(broker);
  Serial.println(" ");

  while (!mqttClient.connect(broker, 8883)) {
    // failed, retry
    Serial.print(".");
    delay(5000);
  }
  Serial.println();

  Serial.println("You're connected to the MQTT broker");
  Serial.println();

  // subscribe to a topic -> 디바이스가 토픽을 구독 
  mqttClient.subscribe("$aws/things/Kiosk_B/shadow/update/delta");   //값이 변경되었을 때 on->off / off->on
}

//사운드, 조도 센서 측정 값 페이로드에 담아주는 함수 
void getDeviceStatus(char* payload) {
  // Read temperature as Celsius (the default)
  light_value = (analogRead(LIGHT_PIN)/10); //1024/10 -> 0~102까지의 범위로 줄여줌
  Serial.print("light value : "); //테스트 프린트 
  Serial.println(light_value);
  
  sound_value = (analogRead(SoundSensor)); //사운드 센서 : 0~1023 범위이지만 감도가 낮은 관계로 일단 그대로 측정 
  Serial.print("sound value : ");
  Serial.println(sound_value);  
  delay(100); //딜레이 주어서 센서 read 안전성 보장

  // Read buzzer status
  const char* buz_status = (buzzer.getState() == BUZZER_ON)? "ON" : "OFF";
  // //const char* led = "ON";

  //make payload for the device update topic ($aws/things/MyMKRWiFi1010/shadow/update) 
  //메시지 브로커를 통해 awsIoT에게  해당 정보를 json형식으로 전달(reported)
  sprintf(payload,"{\"state\":{\"reported\":{\"light\":\"%2d\",\"sound\":\"%2d\",\"buzzer\":\"%s\"}}}",light_value,sound_value,buz_status);
  //페이로드에 각 값을 json 형식으로 담아 준다. 
}

//mqtt 프로토콜을 통해 update 이름으로 메시지 게시하는 함수
void sendMessage(char* payload) {
  char TOPIC_NAME[]= "$aws/things/Kiosk_B/shadow/update";
  //update라는 토픽으로 통지 
  
  Serial.print("Publishing send message:");
  Serial.println(payload);
  mqttClient.beginMessage(TOPIC_NAME);
  mqttClient.print(payload);
  mqttClient.endMessage();
}


void onMessageReceived(int messageSize) {
  // we received a message, print out the topic and contents
  Serial.print("Received a message with topic '");
  Serial.print(mqttClient.messageTopic());
  Serial.print("', length ");
  Serial.print(messageSize);
  Serial.println(" bytes:");

  // // store the message received to the buffer
  char buffer[512] ;
  int count=0;
  while (mqttClient.available()) {
     buffer[count++] = (char)mqttClient.read();
  }
  buffer[count]='\0'; // 버퍼의 마지막에 null 캐릭터 삽입
  Serial.println(buffer); //구독해서 받은 페이로드 문자열 출력 
  Serial.println();

  // // JSon 형식의 문자열인 buffer를 파싱하여 필요한 값을 얻어옴.
  // // 디바이스가 구독한 토픽이 $aws/things/MyMKRWiFi1010/shadow/update/delta 이므로,
  // // JSon 문자열 형식은 다음과 같다.
  // // {
  // //    "version":391,
  // //    "timestamp":1572784097,
  // //    "state":{
  // //        "LED":"ON"
  // //    },
  // //    "metadata":{
  // //        "LED":{
  // //          "timestamp":15727840
  // //         }
  // //    }
  // // }
  // //
  DynamicJsonDocument doc(1024);
  deserializeJson(doc, buffer);
  JsonObject root = doc.as<JsonObject>();
  JsonObject state = root["state"];
  const char* buz_stat = state["buzzer"];
  Serial.println(buz_stat);
  
  char payload[512];
  
  //AWS에 변화된 값을 통지 및 buzzer 상태 바꾸기
  if (strcmp(buz_stat,"ON")==0) {
    buzzer.tone(A_4);    // 라 음계로 소리를 내기 시작
    delay(1000);          // 1초 동안 지속

    buzzer.tone(D_5); // 레 음계로 소리를 내기 시작
    delay(1000);            // 1초 동안 지속
    sprintf(payload,"{\"state\":{\"reported\":{\"buzzer\":\"%s\"}}}","ON");
    sendMessage(payload);
    
  } else if (strcmp(buz_stat,"OFF")==0) {
    buzzer.noTone();
    sprintf(payload,"{\"state\":{\"reported\":{\"buzzer\":\"%s\"}}}","OFF");
    sendMessage(payload);
  }
}

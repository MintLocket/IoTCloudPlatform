1. ### 폴더별 주요 기능
   * #### httpconnection 
      * GetRequest - 디바이스 목록, 상태, 로그 조회시 http response를 받아오는 역할을 수행합니다.
      * PutRequest - 도난 부저 ON / OFF 정보를 http 프로토콜을 통해 PUT 메소드로 요청하는 역할을 수행합니다.
   * #### ui
     * #### apicall 
       * GetLog - 디바이스 로그 조회시에 디바이스의 로그들을 ArrayList로 받아오는 역할을 수행합니다.
       * GetThings - 디바이스 목록 조회 시에 사용자 AWS 계정의 모든 사물들을 가져오는 역할을 수행합니다.
       * GetThingShadow - 디바이스 최신 상태를 가져오는 역할을 수행합니다.
       * UpdateShadow - 디바이스 부저 제어시 ON / OFF 정보를 url 정보로 담아 PutRequest를 호출하는 역할을 수행합니다.
     * ChartActivity - 사용자가 키오스크 A, B 버튼 클릭시 해당 정보에 따라 차트를 그리는 역할을 수행합니다.  
     * DeviceActivity - 디바이스 A, B의 최신 상태를 화면에 표로 제시해줍니다.
     * ListThingsActivity - 디바이스 목록을 화면에 표시해줍니다.
     * LogActivity - 디바이스 A, B의 로그들을 사용자 클릭 이벤트에 따라 띄워주는 역할을 수행합니다. 
     * MainActivity - 메인 화면으로 목록 조회, 상태 조회, 로그 조회, 차트 보기 버튼을 띄우는 역할을 수행합니다.
2. ### 앱 화면
<image height="600" weight="250" src="https://github.com/MintLocket/IoTCloudPlatform/assets/123307856/9476968d-facd-415e-acfd-ba541c77cdf4"></image>
<image height="600" weight="250" src="https://github.com/MintLocket/IoTCloudPlatform/assets/123307856/5ece1255-919a-4618-b877-48e241b2df3b"></image>
<image height="600" weight="250" src="https://github.com/MintLocket/IoTCloudPlatform/assets/123307856/ae478bca-0d22-4c8a-a300-9b38b9beb337"></image>
<image height="600" weight="250" src="https://github.com/MintLocket/IoTCloudPlatform/assets/123307856/4240bbf7-7e86-4152-a03d-628e9dc5a66b"></image>
<image height="600" weight="250" src="https://github.com/MintLocket/IoTCloudPlatform/assets/123307856/35858d9f-5bc6-4f91-b63c-3a268db9100f"></image>
<image height="600" weight="250" src="https://github.com/MintLocket/IoTCloudPlatform/assets/123307856/58172f4c-0614-4906-903d-67b8122d64e3"></image>

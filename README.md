Introduction
=============
Prometheus is an open-source systems monitoring toolkit that you can use with Red Hat Process Automation Manager  to collect and store metrics related to the execution of business rules, processes, Decision Model and Notation (DMN) models, and other Red Hat Process Automation Manager assets. You can access the stored metrics through a REST API call to the KIE Server, through the Prometheus expression browser, or using a data-graphing tool such as Grafana.

Versions
========
EAP: JBOSS 7.3.2
PAM: 7.9


https://access.redhat.com/documentation/en-us/red_hat_process_automation_manager/7.9/html-single/managing_red_hat_process_automation_manager_and_kie_server_settings/index#prometheus-monitoring-con_execution-server


EAP configuration
===================
Download Red Hat SSO from
```
https://access.redhat.com/jbossnetwork/restricted/softwareDownload.html?softwareId=80791
```
and unzip rh-sso-7.4.0.zip

update port-offset in ${RH_SSO_HOME}/standalone/configuration/standalone.xml
from
```
<socket-binding-group name="standard-sockets" default-interface="public" port-offset="${jboss.socket.binding.port-offset:0}">
```
to
```
<socket-binding-group name="standard-sockets" default-interface="public" port-offset="${jboss.socket.binding.port-offset:100}">
```
start Red Hat SSO
```
cd ${RH_SSO_HOME}/bin
./standalone.sh
```
from a browser open
```
http://localhost:8180/auth/
```
create a user example:
```
username: rhssoAdmin
password: Pa$$w0rd
```
use the created account to login at:
```
http://localhost:8180/auth/admin/
```
Import '***contractor-onboarding***' realm:
  * click add realm
  * click import
  * select file [realm-contractor-onboarding.json](configs/realm-contractor-onboarding.json)
  * name : contractor-onboarding
  * click create

Modify identity provider:
  * click identity providers
  * click google
  * set Client ID and Client Secret from OAuth 2.0 Client ID creation
  * click save

Regenerate client secret for service account
  * click clients
  * click kie-server-service-account
  * select the credentials tab
  * click regenerate secret.Copy the secret in [RedHatSSOUtils.java](contractor-onboarding-services/contractor-onboarding-services-api-client/src/main/java/com/redhat/internal/rhsso/RedHatSSOUtils.java)

Import '***realm-redhat-pam***' realm:
  * click add realm
  * click import
  * select file [realm-redhat-pam.json](configs/realm-redhat-pam.json)
  * name : redhat-pam-realm
  * click create

Regenerate client secret for service account
  * click clients
  * click contractor-onboarding
  * select the credentials tab
  * click regenerate secret. Copy the secret in [RedHatSSOUtils.java](contractor-onboarding-commons/src/main/java/com/redhat/internal/rhsso/RedHatSSOUtils.java)

Post SSO config, copy and paste the Redirect URI from the Red Hat Single Sign-On to Google OAuth webclient.

![project modules](images/redirecturi-from-sso.png)

Add Identity Provider page into the Authorized redirect URIs field. 

![project modules](images/google-oauth-redircturi.png)


EAP installation and configuration
==================================

Download the JBOSS EAP from
```
https://access.redhat.com/jbossnetwork/restricted/listSoftware.html?downloadType=distributions&product=appplatform&version=7.2
```
installation instructions found at 
```
https://access.redhat.com/documentation/en-us/red_hat_jboss_enterprise_application_platform/7.3/html/installation_guide/index
```
Run the EAP on port 8080.

Download the PAM from
```
https://access.redhat.com/jbossnetwork/restricted/listSoftware.html?downloadType=distributions&product=rhpam&version=7.7.0
```
and deploy the PAM in above installed EAP. Instalation instructions found at
```
https://access.redhat.com/documentation/en-us/red_hat_process_automation_manager/7.7/html/installing_and_configuring_red_hat_process_automation_manager_on_red_hat_jboss_eap_7.2/index
```

While deploying the PAM create user to interact with business-central
```
username: rhpamAdmin
password: Pa$$w0rd
roles: kie-server, rest-all, admin, process-admin, user
```

Download [Red Hat Single Sign-On 7.4 EAP7 adapter](https://access.redhat.com/jbossnetwork/restricted/softwareDownload.html?softwareId=80771&product=core.service.rhsso)

Follow the documentation to [Integrate Red Hat PAM with Red Hat SSO.](https://access.redhat.com/documentation/en-us/red_hat_process_automation_manager/7.7/html-single/integrating_red_hat_process_automation_manager_with_red_hat_single_sign-on/index#sso-client-adapter-proc)

For additional installation instructions, see the "JBoss EAP Adapter" section of the [Red Hat Single Sign On Securing Applications and Services Guide.](https://access.redhat.com/documentation/en-us/red_hat_single_sign-on/7.3/html-single/securing_applications_and_services_guide/index#jboss_adapter)

```
NOTE: If you are using standalone-full.xml to start (<<EAP_HOME>>/bin$ ./standalone.sh -c standalone-full.xml) the EAP, Install the adapter with the -Dserver.config=standalone-full.xml property.
```

Using [realm-redhat-pam.json](configs/realm-redhat-pam.json) and configure keycloak subsystem as follow
```
  <secure-deployment name="business-central.war">
      <realm>redhat-pam-realm</realm>
      <realm-public-key>MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwjODBWWEAavobrzRsUSC6Fqy5/QGV8vIrOzQrQqqC1U8EeqtkVphqbKEulfEqDRJtcXgNPP3wzXHj5cYwe70X7FYoI9VLcIcV3iok7U9h+/aQ8txllf8jQLTupX+YvhvhqixY9Hs7Q8RmyFF3tlpVx+b3v9iqYkFacB7GfgxvDbIKyXgu7LkWZi8rM224yBRSjC/J/814wv3bbjfap84CijOQlQ4pLwST6NrHCpQEIZQghTjTf8wo36QRGmEWQFTIbi3FVA7sOGy6Qw4uuvV8t5gla8kYjekzjzM//7EFIg3sGIJVzOU99TBCb5re9X0Co+li3Shq7iyPqB3jbmXdQIDAQAB</realm-public-key>
      <auth-server-url>http://localhost:8180/auth/</auth-server-url>
      <ssl-required>EXTERNAL</ssl-required>
      <resource>kie-controller</resource>
      <enable-basic-auth>true</enable-basic-auth>
      <credential name="secret">d9f0d6b4-664e-484b-81c9-7735738c649f</credential>
      <principal-attribute>preferred_username</principal-attribute>
  </secure-deployment>
  <secure-deployment name="kie-server.war">
      <realm>redhat-pam-realm</realm>
      <realm-public-key>MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwjODBWWEAavobrzRsUSC6Fqy5/QGV8vIrOzQrQqqC1U8EeqtkVphqbKEulfEqDRJtcXgNPP3wzXHj5cYwe70X7FYoI9VLcIcV3iok7U9h+/aQ8txllf8jQLTupX+YvhvhqixY9Hs7Q8RmyFF3tlpVx+b3v9iqYkFacB7GfgxvDbIKyXgu7LkWZi8rM224yBRSjC/J/814wv3bbjfap84CijOQlQ4pLwST6NrHCpQEIZQghTjTf8wo36QRGmEWQFTIbi3FVA7sOGy6Qw4uuvV8t5gla8kYjekzjzM//7EFIg3sGIJVzOU99TBCb5re9X0Co+li3Shq7iyPqB3jbmXdQIDAQAB</realm-public-key>
      <auth-server-url>http://localhost:8180/auth/</auth-server-url>
      <ssl-required>EXTERNAL</ssl-required>
      <resource>kie-execution-server</resource>
      <enable-basic-auth>true</enable-basic-auth>
      <credential name="secret">059d24bb-9401-4411-950b-7dff45a63545</credential>
      <principal-attribute>preferred_username</principal-attribute>
  </secure-deployment>
```

#### EAP System properties

```
Add the following EAP system properties in standalone-full.xml (If you are using standalone-full.xml to start)
<property name="org.kie.server.user" value="rhpamAdmin"/>
<property name="org.kie.server.pwd" value="Pa$$w0rd”/>
<property name="org.kie.server.controller.user" value="rhpamAdmin"/>
<property name="org.kie.server.controller.pwd" value="Pa$$w0rd"/>
```

#### Import Contractor-Onboarding process in PAM 
To import Contractor-Onboarding process follow instructions [here](https://gitlab.consulting.redhat.com/contractor-onboarding/contractor-onboarding-process)


Enable google APIs
==================

  * Create a new project<br />
  In a browser open [Google cloud resource manager](https://console.developers.google.com/cloud-resource-manager) and crete a new project<br />
  ![create new project](images/create-new-project.png)<br />
  * Create a service account
  In a browser open [Google credential manager](https://console.developers.google.com/apis/credentials) and click 'Create Credentials'<br />
  ![create credentials](images/create-credentials.png)<br />
  Select 'Service account'<br />
  ![select service account](images/select-service-account.png)<br />
  Choose a name for the service account(example 'contractor-onboarding-sa')<br />
  ![service account details](images/servicec-account-details.png)<br />
  Grant the service account as project owner<br />
  ![service account grant](images/service-account-grant.png)<br />
  Create a key<br />
  ![create key](images/create-a-key.png)<br />
  Select JSON format and click 'Create' to generate the key<br />
  ![key in json format](images/key-in-json-format.png)<br />
  Save the generated json file in [credentials.json](contractor-onboarding-services/contractor-onboarding-services-api-impl/src/main/resources/credentials.json)<br />
  * Enable Google API library for the project
  In a browser open [Google API library](https://console.developers.google.com/apis/library)<br />
  In the search abr type __'Gmail API'__<br />
  ![Search](images/search-api.png)<br />
  Select the api and enable it<br />
  ![enable](images/enable-api.png)<br />
  repeat this operation to enable also __'Google Docs API'__, __'Google Sheets API'__, __'Google Drive API'__<br />

Create VAA Template
In google drive create a new google docs with the following text<br />
```
Ciao ${username},
Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged.

${username}

${manager_username}
```
copy the docs id from the url (in my case is '1QTQuWRylvjpsvLzKbRtjGSwyE_gf13T74u4hb9VJh2U')<br />
![document id](images/document-id.png)<br />
and replace the value of the property ```com.redhat.internal.integrations.google.template-file-id``` in the properties file [application.properties](contractor-onboarding-services/contractor-onboarding-services-api-impl/src/main/resources/application.properties)<br />
share the created template with the email defined in the [credentials.json](contractor-onboarding-services/contractor-onboarding-services-api-impl/src/main/resources/credentials.json)<br />
![share docs](images/share-docs-with-service-account.png)<br />

Configure the output directory
Set the value of the property ```com.redhat.internal.integrations.google.pdf-output-path-template``` in the properties file [application.properties](contractor-onboarding-services/contractor-onboarding-services-api-impl/src/main/resources/application.properties) to the directory you want to store generated pdf<br />


```
Note: Due to project limitations, google APIs integrations was ruled out. Most of the java code related to integrations was commented. 
```

#### build dependencies
Build dependencies from [here](https://gitlab.consulting.redhat.com/contractor-onboarding/redhat-internal-integrations)
```
$ cd redhat-internal-integrations/
$ mvn clean install
```

#### build project
```
$ cd contractor-onboarding-parent/
$ mvn clean install
```

#### Start local Quarkus application in Dev mode
Configure properties file [application.properties](contractor-onboarding-services/contractor-onboarding-services-api-impl/src/main/resources/application.properties) with SSO and KIE server URL 
```
$ cd contractor-onboarding-parent/contractor-onboarding-services/contractor-onboarding-services-api-impl/
$ ./mvnw compile quarkus:dev
```

#### Test service account 
From postman create new request:
```
http://localhost:8280/contractor-onboarding/private/query/contractorsBySurname?surname=luppi
```
In the 'Pre-request Scripts' tab copy and paste the following script
```
var server       = "http://localhost:8180/";
var realm        = "contractor-onboarding";
var grantType    = "client_credentials";
var clientId     = "kie-server-service-account";
var clientSecret = "<HERE_INSERT_THE_SECRET>";

var url  = `${server}auth/realms/${realm}/protocol/openid-connect/token`;
var data = `grant_type=${grantType}&client_id=${clientId}&client_secret=${clientSecret}`;

pm.sendRequest({
    url: url,
    method: 'POST',
    header: { 'Content-Type': 'application/x-www-form-urlencoded'},
    body: {
        mode: 'raw',
        raw: data
    }
},  function(err, response) {
    
    var response_json = response.json();
    var token = response_json.access_token;
    pm.environment.set('token', token);
    console.log(token);
});
```
![postman token generator script](images/postman-token-generator-script.png)<br />
In the 'Authorization' tab select 'Bearer Token' as type and insert '{{token}}' as Token<br />
![postman authorization](images/postman-authorization.png)<br />
Click Send and check code of the response shuld be '200 OK' and a json is returned as body<br />

Additional information
======================


#### deploy project
Provide the server details in maven seetings xml to deploy the artifacts in remote repository
```
$ mvn deploy -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true -Dmaven.wagon.http.ssl.ignore.validity.dates=true -DPAM_NEXUS_URL=<<NEXUS/Repository URL>>
```

#### build native
```
$ cd contractor-onboarding-parent/contractor-onboarding-services/contractor-onboarding-services-api-impl/
$ ./mvnw install -Dnative
```


#### run jar
```
cd contractor-onboarding-parent/contractor-onboarding-services/contractor-onboarding-services-api-impl/
$ java -jar -Dcom.redhat.internal.services.remoteClasses=<<comma separated remotable classes>> ./target/contractor-onboarding-services-api-impl-<PROJECT_VERSION>-runner.jar 

Example: java -jar -Dcom.redhat.internal.services.remoteClasses=com.redhat.internal.model.process.ContractorDetails,org.acme.order_fulfillment.ProductOrder ./target/contractor-onboarding-services-api-impl-1.0.0-SNAPSHOT-runner.jar 
```


Note: Comma separated remotable classes should be in class path of [contractor-onboarding-services-api-impl](contractor-onboarding-parent/contractor-onboarding-services/contractor-onboarding-services-api-impl) 
i.e. add remotable classes project as dependency for [pom.xml](contractor-onboarding-services/contractor-onboarding-services-api-impl/src/main/resources/pom.xml)



#### run native
```
cd contractor-onboarding-parent/contractor-onboarding-services/contractor-onboarding-services-api-impl/
$ ./target/contractor-onboarding-services-api-impl-<PROJECT_VERSION>-runner
```


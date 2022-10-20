# ad hoc file transfer

feature：
1. convenient: no login needed
2. safe: no file persistent on server
3. safe：must download in one minute after upload and download only once
4. and safe: 6-digit letter verification code which means 308,915,776 possibilities

## upload

just upload it

# download

input the code from upload page, then press submit

# when tomcat 9 always add charset=ISO-8859-1 into response header

add into tomcat/conf/web.xml
```
<filter>
    <filter-name>AddDefaultCharsetFilter</filter-name>
    <filter-class>org.apache.catalina.filters.AddDefaultCharsetFilter</filter-class>
    <init-param>
        <param-name>encoding</param-name>
        <param-value>UTF-8</param-value>
    </init-param>
</filter>
<filter-mapping>
    <filter-name>AddDefaultCharsetFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```
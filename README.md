
## Disclaimer
It's just an experiment with customization of maven-resolver-transport-wagon to use in maven build.

How to install
Define `.mvn/extensions.xml` in the project
```xml
<?xml version="1.0" encoding="UTF-8"?>
<extensions>
    <extension>
        <groupId>com.miro.maven</groupId>
        <artifactId>maven-custom-resolver</artifactId>
        <version>1.0-SNAPSHOT</version>
    </extension>
</extensions>
```

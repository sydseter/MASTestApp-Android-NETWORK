# Uncrakcable - Level 1-2

An app with a really bad networking issue

## Insecure CA

The CA is created from this guide: https://gist.github.com/soarez/9688998
All certificate use the passphrase: test

# Create Revocation list
    $ openssl ca -config valid.ca.conf -gencrl -out crl/root.crl
    # Enter pass phrase for ./ca.key:test


### Creating a valid certificate from an insecure CA for tlsrevocation.org

These instructions where made from git bash. You can use Linux, Mac OS Terminal or Ubuntu for windows as well
It will create a certificate that is valid for 100 years and register the certificate with the fake CA.

    # Move into the ca directory
    cd ca

    # Generate the key
    $ openssl genrsa -out valid/tlsrevocation.org.key 4096

    # Create the signing request
    $ openssl req -new -key valid/tlsrevocation.org.key -out valid/tlsrevocation.org.csr
    
    You are about to be asked to enter information that will be incorporated
    into your certificate request.
    What you are about to enter is what is called a Distinguished Name or a DN.
    There are quite a few fields but you can leave some blank
    For some fields there will be a default value,
    If you enter '.', the field will be left blank.
    -----
    Country Name (2 letter code) [AU]:NO
    State or Province Name (full name) [Some-State]:Drammen
    Locality Name (eg, city) []:Drammen
    Organization Name (eg, company) [Internet Widgits Pty Ltd]:Insecure CA
    Organizational Unit Name (eg, section) []:Insubordinate
    Common Name (e.g. server FQDN or YOUR name) []:tlsrevocation.org
    Email Address []:insecure@tlsrevocation.org

    Please enter the following 'extra' attributes
    to be sent with your certificate request
    A challenge password []:
    An optional company name []:

    # Sign the intermediate certificate with the certificate of the ca
    $ openssl ca -batch -config valid.ca.conf -notext -in valid/tlsrevocation.org.csr -out valid/tlsrevocation.org.crt
    # Enter pass phrase for ./ca.key:test

    # Create a pkcs12 certificate file
    $ openssl pkcs12 -export -out valid/tlsrevocation.org.p12 -inkey valid/tlsrevocation.org.key -in valid/tlsrevocation.org.crt -chain -CAfile ca.crt
    # Enter Export Password:test
    # Verifying - Enter Export Password:test
    

    # Check certificate status
    openssl verify -extended_crl -verbose -CRLfile crl/root.crl -CAfile ca.crt -crl_check valid/tlsrevocation.org.crt
    # valid/tlsrevocation.org.crt: OK
    openssl pkcs12 -in valid/tlsrevocation.org.p12 -nodes -out tmp.key
    openssl s_client -connect gateway.push.apple.com:2195 -cert tmp.key -key tmp.key

### Creating a expired certificate from an insecure CA for tlsexpired.no

These instructions where made from git bash. You can use Linux, Mac OS Terminal or Ubuntu for windows as well
It will create a certificate that is valid for one day and register the certificate with the fake CA.

    # Move into the ca directory
    cd ca

    # Generate the key
    $ openssl genrsa -out tlsexpired/tlsrevocation.org.key 4096

    # Create the signing request
    $ openssl req -new -key tlsexpired/tlsrevocation.org.key -out tlsexpired/tlsrevocation.org.csr
    
    You are about to be asked to enter information that will be incorporated
    into your certificate request.
    What you are about to enter is what is called a Distinguished Name or a DN.
    There are quite a few fields but you can leave some blank
    For some fields there will be a default value,
    If you enter '.', the field will be left blank.
    -----
    Country Name (2 letter code) [AU]:NO
    State or Province Name (full name) [Some-State]:Drammen
    Locality Name (eg, city) []:Drammen
    Organization Name (eg, company) [Internet Widgits Pty Ltd]:Insecure CA
    Organizational Unit Name (eg, section) []:Insubordinate
    Common Name (e.g. server FQDN or YOUR name) []:tlsexpired.no
    Email Address []:insecure@tlsrevocation.org

    Please enter the following 'extra' attributes
    to be sent with your certificate request
    A challenge password []:
    An optional company name []:

    # Sign the intermediate certificate with the certificate of the ca
    $ openssl ca -batch -config tlsexpired.ca.conf -notext -in tlsexpired/tlsrevocation.org.csr -out tlsexpired/tlsrevocation.org.crt
    # Enter pass phrase for ./ca.key:test

    # Create a pkcs12 certificate file
    $ openssl pkcs12 -export -out tlsexpired/tlsrevocation.org.p12 -inkey tlsexpired/tlsrevocation.org.key -in tlsexpired/tlsrevocation.org.crt -chain -CAfile ca.crt
    # Enter Export Password:test
    # Verifying - Enter Export Password:test

    # Check certificate status
    openssl verify -extended_crl -verbose -CRLfile crl/root.crl -CAfile ca.crt -crl_check tlsexpired/tlsrevocation.org.crt
    # tlsexpired/tlsrevocation.org.crt: OK
    openssl pkcs12 -in tlsexpired/tlsrevocation.org.p12 -nodes -out tmp.key
    openssl s_client -connect gateway.push.apple.com:2195 -cert tmp.key -key tmp.key


### Creating a certificate with a wrong subject alt name from an insecure CA for tlsbadsubjectaltname.no

These instructions where made from git bash. You can use Linux, Mac OS Terminal or Ubuntu for windows as well
It will create a certificate that is valid for one day and register the certificate with the fake CA.

    # Move into the ca directory
    cd ca

    # Generate the key
    $ openssl genrsa -out tlsbadsubjectaltname/tlsrevocation.org.key 4096

    # Create the signing request
    $ openssl req -new -key tlsbadsubjectaltname/tlsrevocation.org.key -out tlsbadsubjectaltname/tlsrevocation.org.csr
    
    You are about to be asked to enter information that will be incorporated
    into your certificate request.
    What you are about to enter is what is called a Distinguished Name or a DN.
    There are quite a few fields but you can leave some blank
    For some fields there will be a default value,
    If you enter '.', the field will be left blank.
    -----
    Country Name (2 letter code) [AU]:NO
    State or Province Name (full name) [Some-State]:Drammen
    Locality Name (eg, city) []:Drammen
    Organization Name (eg, company) [Internet Widgits Pty Ltd]:Insecure CA
    Organizational Unit Name (eg, section) []:Insubordinate
    Common Name (e.g. server FQDN or YOUR name) []:wrongname.no
    Email Address []:insecure@tlsrevocation.org

    Please enter the following 'extra' attributes
    to be sent with your certificate request
    A challenge password []:
    An optional company name []:

    # Sign the intermediate certificate with the certificate of the ca
    $ openssl ca -batch -config tlsbadsubjectaltname.ca.conf -notext -in tlsbadsubjectaltname/tlsrevocation.org.csr -out tlsbadsubjectaltname/tlsrevocation.org.crt
    # Enter pass phrase for ./ca.key:test

    # Create a pkcs12 certificate file
    $ openssl pkcs12 -export -out tlsbadsubjectaltname/tlsrevocation.org.p12 -inkey tlsbadsubjectaltname/tlsrevocation.org.key -in tlsbadsubjectaltname/tlsrevocation.org.crt -chain -CAfile ca.crt
    # Enter Export Password:test
    # Verifying - Enter Export Password:test

    # Check certificate status
    openssl verify -extended_crl -verbose -CRLfile crl/root.crl -CAfile ca.crt -crl_check tlsbadsubjectaltname/tlsrevocation.org.crt
    # tlsbadsubjectaltname/tlsrevocation.org.crt: OK
    openssl pkcs12 -in tlsbadsubjectaltname/tlsrevocation.org.p12 -nodes -out tmp.key
    openssl s_client -connect gateway.push.apple.com:2195 -cert tmp.key -key tmp.key

### Creating a revoked certificate from an insecure CA for tlsrevoked.no

These instructions where made from git bash. You can use Linux, Mac OS Terminal or Ubuntu for windows as well
It will create a certificate that is valid for one day and register the certificate with the fake CA.

    # Move into the ca directory
    cd ca

    # Generate the key
    $ openssl genrsa -out tlsrevoked/tlsrevocation.org.key 4096

    # Create the signing request
    $ openssl req -new -key tlsrevoked/tlsrevocation.org.key -out tlsrevoked/tlsrevocation.org.csr
    
    You are about to be asked to enter information that will be incorporated
    into your certificate request.
    What you are about to enter is what is called a Distinguished Name or a DN.
    There are quite a few fields but you can leave some blank
    For some fields there will be a default value,
    If you enter '.', the field will be left blank.
    -----
    Country Name (2 letter code) [AU]:NO
    State or Province Name (full name) [Some-State]:Drammen
    Locality Name (eg, city) []:Drammen
    Organization Name (eg, company) [Internet Widgits Pty Ltd]:Insecure CA
    Organizational Unit Name (eg, section) []:Insubordinate
    Common Name (e.g. server FQDN or YOUR name) []:tlsrevoked.no
    Email Address []:insecure@tlsrevocation.org

    Please enter the following 'extra' attributes
    to be sent with your certificate request
    A challenge password []:
    An optional company name []:

    # Sign the intermediate certificate with the certificate of the ca
    $ openssl ca -batch -config tlsrevoked.ca.conf -notext -in tlsrevoked/tlsrevocation.org.csr -out tlsrevoked/tlsrevocation.org.crt
    # Enter pass phrase for ./ca.key:test

    # Create a pkcs12 certificate file
    $ openssl pkcs12 -export -out tlsrevoked/tlsrevocation.org.p12 -inkey tlsrevoked/tlsrevocation.org.key -in tlsrevoked/tlsrevocation.org.crt -chain -CAfile ca.crt
    # Enter Export Password:test
    # Verifying - Enter Export Password:test
    

    # Check certificate status
    openssl verify -extended_crl -verbose -CRLfile crl/root.crl -CAfile ca.crt -crl_check tlsrevoked/tlsrevocation.org.crt
    # tlsrevoked/tlsrevocation.org.crt: OK
    openssl pkcs12 -in tlsrevoked/tlsrevocation.org.p12 -nodes -out tmp.key
    openssl s_client -connect gateway.push.apple.com:2195 -cert tmp.key -key tmp.key

    # Extract certificate and key if you need to upload them to your domain.
    # NB: You may need to upload the CA crt as well (ca.crt)
    # PKCS#1 Private key
    openssl pkcs12 -in tlsrevoked/tlsrevocation.org.p12 -nocerts -out tlsrevoked/tlsrevocation.org.key.pem -passout pass:
    # Certificates:
    openssl pkcs12 -in tlsrevoked/tlsrevocation.org.p12 -clcerts -nokeys -out tlsrevoked/tlsrevocation.org.crt.pem -passout pass:

    # Revoke the certificate
    openssl ca -config tlsrevoked.ca.conf -revoke tlsrevoked/tlsrevocation.org.crt
    # Enter pass phrase for ./ca.key:test

    # update the CRL
    openssl ca -config tlsrevoked.ca.conf -gencrl -out crl/root.crl

### Place the crl together with your web files

    # Move the fake crl to your vulnerable domain
    #
    cp crl/root.crl ../web
    #
    # Deploy web to your domain of choice

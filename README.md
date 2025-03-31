# Uncrakcable - Level 1-2

An app with a really bad networking issue

## Insecure CA

The CA is created from this guide: https://blog.didierstevens.com/2013/05/08/howto-make-your-own-cert-and-revocation-list-with-openssl/
All certificate use the passphrase: test


### Creating a (soon to be) invalid ICA for tlsrevocation.org

These instructions where made from git bash. You can use Linux, Mac OS Termnial or Ubuntu for windows as well
It will create a certificate that is valid for one day and register the certificate with the fake CA.

    # Move into the ca directory
    cd ca

    # Generate the key
    $ openssl genrsa -out invalid/tlsrevocation.org.key 4096    

    # Create the signing request
    $ openssl req -new -key invalid/tlsrevocation.org.key -out invalid/tlsrevocation.org.csr
    
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
    Organization Name (eg, company) [Internet Widgits Pty Ltd]:Insecure ICA
    Organizational Unit Name (eg, section) []:Insubordinate
    Common Name (e.g. server FQDN or YOUR name) []:tlsrevocation.org
    Email Address []:johan@sydseter.com

    Please enter the following 'extra' attributes
    to be sent with your certificate request
    A challenge password []:test
    An optional company name []:

    # Sign the intermediate certificate with the certificate of the ca
    $ openssl ca -batch -config invalid.ca.conf -notext -in invalid/tlsrevocation.org.csr -out invalid/tlsrevocation.org.crt

    # Create a pkcs12 certificate file
    $ openssl pkcs12 -export -out invalid/tlsrevocation.org.p12 -inkey invalid/tlsrevocation.org.key -in invalid/tlsrevocation.org.crt -chain -CAfile ca.crt

    # Update Revocation list
    $ openssl ca -config invalid.ca.conf -gencrl -out crl/root.crl

    # Check certificate status
    openssl verify -extended_crl -verbose -CRLfile crl/root.crl -CAfile ca.crt -crl_check valid/tlsrevocation.org.crt

    # Move the fake crl to your vulnerable domain
    cp crl/root.crl ../web
# Cluster File Format Specification

This document describes the format of serialized cluster files. It is arranged as a sequential list of the data that is stored in a file.

* 4 bytes to indicate the version of the cluster file as an integer.
* 1 byte with value 1 to indicate that the contents are encrypted, or 0 if unencrypted.
* If encrypted:
    * 8 bytes contain the salt value used when encrypting.
    * 16 bytes contain the initialization vector used when encrypting.
* The rest of the file contains the cluster content.
# CrystalKeep

A comprehensive solution for encrypted data storage.

# Features

- Simple Key -> Value storage
- Username / password logins
- Files / Directories

# Design

CrystalKeep makes use of _Shards_ as the most basic form of encrypted data storage. A shard is a single data item, like login credentials, an image, or some text. One or more shards are stored in a _Cluster_, which is essentially a collection of shards (and possibly nested clusters). Top-level clusters (not nested inside another) can be encrypted and saved with a secret key passphrase.

With this approach, the user minimizes the amount of data that is accessible to an attacker in the event that the attacker gets access to the system while the contents of a cluster are unencrypted in memory, since only one cluster may be actively open at a time.
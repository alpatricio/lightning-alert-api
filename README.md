# lightning-alert-api
Lightning Alert App

This Java Spring Boot App exposes an HTTP endpoint which accepts lightning strike info in the form of file and alerts user if a known asset has been stricken

example request cURL
```json
curl --location --request POST 'localhost:8080/lightning-alert' \
--form 'file=@"/IdeaProjects/lightning-alert-api/lightning.json"'
```
Find lightning.json in repo as sample request file

An example 'strike' coming off of the exchange looks like this:

```json
{
    "flashType": 1,
    "strikeTime": 1386285909025,
    "latitude": 33.5524951,
    "longitude": -94.5822016,
    "peakAmps": 15815,
    "reserved": "000",
    "icHeight": 8940,
    "receivedTime": 1386285919187,
    "numberOfSensors": 17,
    "multiplicity": 1
}
```

### Where:

- flashType=(0='cloud to ground', 1='cloud to cloud', 9='heartbeat')
- strikeTime=the number of milliseconds since January 1, 1970, 00:00:00 GMT

### Note

- A 'heartbeat' flashType is not a lightning strike. It is used internally by the software to maintain connection.

An example of an 'asset' is as follows:

```json
  {
    "assetName":"Dante Street",
    "quadKey":"023112133033",
    "assetOwner":"6720"
  }
```

---

You might notice that the lightning strikes are in lat/long format, whereas the assets are listed in quadkey format.

Conversion process, outlined [here](http://msdn.microsoft.com/en-us/library/bb259689.aspx)
For each strike received, you should simply print to the console the following message:

```log
lightning alert for <assetOwner>:<assetName>
```

But substituting the proper assetOwner and assetName.

i.e.:

```log
lightning alert for 6720:Dante Street
```

Alert will only happen once per asset

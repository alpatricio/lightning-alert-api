# lightning-alert-api
Lightning Alert App

This Java Spring Boot App exposes an endpoint which accepts lightning strike info in the form of file and alerts user if a known asset has been stricken

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

There is a simple conversion process, outlined [here](http://msdn.microsoft.com/en-us/library/bb259689.aspx) that you can take advantage of. Feel free to use an open source library as well.

# wmv-length

Inspects header of WMV file and outputs its length in secnods.

```shell
% wmv-length ./output_test-60.wmv
{"status":"ok","durationSec":60}
```

```shell
% wmv-length ./brokenfile
{"status":"err","message":"preamble (whether WMV or not): expected constant BitVector(32 bits, 0x3026b275) but got BitVector(32 bits, 0x12345678)"}
```

Spec of WMV (ASF) format: http://web.archive.org/web/20140419205228/http://msdn.microsoft.com/en-us/library/bb643323.aspx
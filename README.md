# Tree of Savior packer/unpacker
Description: console utility for packing/unpacking resources for Tree Of Savior.

## Unpacker
`java -cp tos-resource-2.0-jar-with-dependencies.jar ru.rage.tos.resource.Unpacker -out OUTPUT -in INPUT`
**OUTPUT** - output folder like: "./unpack"
**INPUT** - input folder or file ipf format like: "./pack" or "./file.ipf"

## Packer
`java -cp tos-resource-2.0-jar-with-dependencies.jar ru.rage.resource.Packer -in INPUT`
**INPUT** - input folder; folder must have file struct like unpacker result, example:
```
- input - archive.ipf -|- dir1
					   |- dir2
					   |- ...
					   |- file1
					   |- file2
					   |- ...
```

## IES Worker
### Unpacker
`java -cp tos-resource-2.0-jar-with-dependencies.jar ru.rage.tos.resource.IesWorker -file INPUT -handler HANDLER`
**INPUT** - input folder or file
**HANDLER** - path to class with implements IIesHandler (ex: `ru.rage.tos.resource.test.IesTestHandler`)

### Packer
`java -cp tos-resource-2.0-jar-with-dependencies.jar ru.rage.tos.resource.IesWorker -file INPUT -pack -handler HANDLER`
**INPUT** - input folder or file
**HANDLER** - path to class with implements IIesHandler (ex: `ru.rage.tos.resource.test.IesTestHandler`)

## Credits
Author: Pointer*Rage
Thanks: luna9966, Deazer, h4x0r

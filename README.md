# Tree of Savior packer/unpacker
Console utility for packing/unpacking resources for Tree Of Savior.

## Unpacker
`java -cp tos-resource-2.0-jar-with-dependencies.jar ru.rage.tos.resource.Unpacker -out OUTPUT -in INPUT`
1. **OUTPUT** - output folder like: "./unpack"
2. **INPUT** - input folder or file ipf format like: "./pack" or "./file.ipf"

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
1. **INPUT** - input folder or file
2. **HANDLER** - path to class with implements IIesHandler (ex: `ru.rage.tos.resource.test.IesTestHandler`)

### Packer
`java -cp tos-resource-2.0-jar-with-dependencies.jar ru.rage.tos.resource.IesWorker -file INPUT -pack -handler HANDLER`
1. **INPUT** - input folder or file
2. **HANDLER** - path to class with implements IIesHandler (ex: `ru.rage.tos.resource.test.IesTestHandler`)

## Credits
Author: Pointer*Rage

Thanks: luna9966, Deazer, h4x0r

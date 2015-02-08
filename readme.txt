----------------
Requred: JRE 8 (oracle.com)
----------------
Description: console utility for packing/unpacking resources for Tree Of Savior
----------------

Unpacker:
java -cp tos_resource_v2.jar ru.rage.tos.resource.Unpacker -out OUTPUT -in INPUT

OUTPUT - output folder like: "./unpack"
INPUT - input folder or file ipf format like: "./pack" or "./file.ipf"

Packer:
java -cp tos_resource_v2.jar ru.rage.resource.Packer -in INPUT

INPUT - input folder; folder must have file struct like unpacker result, ex:
- input - archive.ipf -|- dir1
                       |- dir2
					   |- ...
					   |- file1
					   |- file2
					   |- ...

----------------
IES Worker

Unpacker:
java -cp tos_resource_v2.jar -file INPUT -handler HANDLER

INPUT - input folder or file
HANDLER - path to class with implements IIesHandler (ex: ru.rage.tos.resource.test.IesTestHandler)

Packer:
java -cp tos_resource_v2.jar -file INPUT -pack -handler HANDLER

INPUT - input folder or file
HANDLER - path to class with implements IIesHandler (ex: ru.rage.tos.resource.test.IesTestHandler)

----------------
@Author: Pointer*Rage
@Thanks: luna9966, Deazer, h4x0r
----------------
# roundforest-csv-analyzer

A program analyzing product reviews

As discussed it seems like a good choice to use a batching framework for this program such as Spark but since I don't have a hands on experience in such framework I did it with Java executor services providing a multi threaded non distributed capabilities. Of course it can be expanded to have distributed capabilities in a few ways.

I used testNG as a testing framework. I made one unit test as an example.

I used a HashSet to overlook duplicates by Id, of course there can be more logical duplicates but I didn't handled them.

I gave the program 500MB as max memory in the run command. In order to track memory size I converted all saved strings into bytes and counted number of bytes (I ignored saved numbers here) I print an alert when memory reaches 400MB, of course it can be handled in few different ways.

Due to lack of time I didn't implement a mock for the translation service but returned the string as is, therefore I don't look for the translation argument as well. I can add improvements if it's crucial.

I also didn't used any logging framework and didn't handled exceptions. Everything is printed to stdout.

I worked on the test on and off in an estimated time of up to 4-5 hours

I am available to discuss the decisions I made.

Running instructions:
go to project dir
run the following commands: 

mvn clean install
cd target
unzip roundforest-csv-analyzer-0.0.1-SNAPSHOT-app.zip
java -Xmx512M -DcsvFile=/path/to/file/Reviews.csv -cp ./*:./lib/* origrad.roundforest.csv.analyzer.Main 



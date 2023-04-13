# Intro Homework
## Description
Use Python, Scala or Java to produce a CSV file with top five word 3-grams (n-grams, use lowercase and remove punctuation) 
in the commit messages for each author name in event type “PushEvent” within the file 10K.github.jsonl.bz2. 

#### Output example:

> `'author' 'first 3-gram' 'second 3-gram' 'third 3-gram' 'fourth 3-gram' 'fifth 3-gram'`

> `'erfankashani' 'merge pull request' 'pull request #4' 'request #4 from' 'rack from 207' 'from 207 to'`

## How to run

#### Environment
Maven and Java 17 should be installed.

#### Build
>`mvn clean package`

#### Run
>`mvn exec:java`

To run this program with other input file use the command like:
>`mvn exec:java -Dexec.args="file:///c:/work/other-file.jsonl.bz2"`

#### Results
You can find execution result in the `./output` folder. 
The exact filename is displayed in the program execution log.

#### Example
Successful execution log looks like this
```
15:59:59.625 [com.epam.kramanenka.trigram.Main.main()] INFO com.epam.kramanenka.trigram.Main - Program execution started
15:59:59.632 [com.epam.kramanenka.trigram.Main.main()] INFO com.epam.kramanenka.trigram.util.FileUtils - Output file is created: C:\work\DATA-ENG\git\m01_intro_top_five_trigram\output\1681390799631-top-five-trigrams.csv
15:59:59.707 [com.epam.kramanenka.trigram.Main.main()] INFO com.epam.kramanenka.trigram.TrigramFinder - Reading input file C:\work\DATA-ENG\git\m01_intro_top_five_trigram\target\classes\10K.github.jsonl.bz2
16:00:01.463 [com.epam.kramanenka.trigram.Main.main()] INFO com.epam.kramanenka.trigram.TrigramFinder - Analyzing data and writing output file output\1681390799631-top-five-trigrams.csv
16:00:01.664 [com.epam.kramanenka.trigram.Main.main()] INFO com.epam.kramanenka.trigram.Main - Finished in 00:00:02
```

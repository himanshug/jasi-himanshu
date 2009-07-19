if [ X$1 = 'X-debug' ]
then
  java -Djava.util.logging.config.file=src/main/resources/logger.properties -jar target/jasi-0.0.jar
else
  java -jar target/jasi-0.0.jar
fi



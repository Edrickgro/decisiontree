PROJECT = decisiontree
RUNCOMMAND = decisiontree.App

######## DO NOT MODIFY ANYTHING BELOW THIS LINE #######
COURSEDIR = /course/cs0160

include $(COURSEDIR)/include/Makefile.common



# RUN_TESTS = javac -cp /course/cs0160/lib/nds4/nds4.jar:/course/cs0160/lib/junit-4.12.jar:/course/cs016/lib/hamcrest-core-1.3.jar:/course/cs016/lib/cs016.jar:.:.. decisiontree.TestRunner

RUN_TESTS = java -cp $(CLASSPATH) decisiontree.TestRunner

run_tests:
		$(RUN_TESTS)

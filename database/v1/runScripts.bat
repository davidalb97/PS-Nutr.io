::Use red color for output problems
echo -e "$RED"

::Deletes previous compacted file (if last execution was aborted before file deletion)
del .\compactedfiles.out /F

::Sets output to only display errors from file execution
echo "SET client_min_messages = error;" >> ./compactedfiles.out

::Compacts all files into one
for FILE in `ls -1 -d *.sql **/*.sql`
do
  cat $FILE >> .\compactedfiles.out
done

::-1 transaction execution
::-q quiet mode
::-f script file to execute
psql -1 -q -f compactedfiles.out "dbname='${PS_POSTGRES_DB}' user='${PS_POSTGRES_USER}' password='${PS_POSTGRES_PASSWORD}'"

::Deletes compacted file
del .\compactedfiles.out /F

pause
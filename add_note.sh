NOTE_FILE_DIR=documents/notes.txt

RIGHT_NOW=$(date +"%x %r %Z")
TIME_STAMP="Added on $RIGHT_NOW"

echo "---------------------------" >> $NOTE_FILE_DIR
echo $TIME_STAMP >> $NOTE_FILE_DIR
echo $1 >> $NOTE_FILE_DIR
echo "" >> $NOTE_FILE_DIR
echo "---------------------------" >> $NOTE_FILE_DIR

# This is simply to convert all the file names into commands for use in LaTeX - it should save a decent amount of time (hopefully)

#\newcommand{} {\includegraphics[height=\cardheight]{cards/10_of_clubs.png}}

f = open("cards/out")

o = open("newout", "w")

for x in f:

    line = x[:-1] # the actual line, stripped of a \n
    fName = str(line[57:-2]) # includes .png extension - for getting filename
    strpName = fName.replace("_of_", "")[:-4] # removes .png

    print(line)
    print(strpName)
    command = "\\" + strpName # prepends \ to command

    insertTo = 0 # where to insert in the string 

    new = line[:insertTo] + command + line[insertTo:] + "\n"

    print("Writing: " + new[:-1])

    print()

 #   o.write(new)

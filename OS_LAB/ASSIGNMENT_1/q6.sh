#! /bin/bash

read -p "Enter Filename: " filename
read -p "Enter Word to Search in file: " string

echo
freq=$(grep -o $string $filename | wc -l)
if [ $freq == 0 ]
then
	echo "No \"$string\" found in file \"$filename\""
	exit 1
fi

echo "Frequency of occurence of word '$string' in file '$filename' is : $freq"

freqarr=(`grep -o -n $string $filename | cut -d : -f 1 | uniq -c`)

echo
echo "Table showing the line wise frequencies of '$string'"
echo "----------------------------------------------------"
echo -e "\tLine number \tFrequency"
echo "----------------------------------------------------"

for(( i=0; i<${#freqarr[@]}; i+=2 ))
do 
	echo -e "\t${freqarr[$i+1]} \t\t${freqarr[$i]}"
done
echo "----------------------------------------------------"

echo


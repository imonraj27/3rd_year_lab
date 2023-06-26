#! /bin/bash

echo "Enter uv1 and uv2 values: "
read uv1 uv2

echo $uv1 , $uv2
echo $uv2 and $uv1


# FINDING LENGTH OF VARS
len1=${#uv1}
len2=${#uv2}

echo -n "uv1 in reverse is: "

for (( i=$len1-1; i>=0; i-- )) # PRINTING IN REVERSE
do
	echo -n ${uv1:i:1}
done

echo 

echo -n "uv2 in reverse is: "

for (( i=$len2-1; i>=0; i-- )) # PRINTING IN REVERSE
do
	echo -n ${uv2:i:1}
done

#! /bin/bash
recycle_bin="my-deleted-files"

countIt(){ #counts number of versions present already in recycle bin
			# and return the final to be inserted version
	vari=$1
	flnm=""
	extn=""
	strlen=${#vari}
	
	for (( c=0; c<$strlen; c++ )); do 
   	if [ ${vari:c:1} = "." ]
   	then 
   		extn+=${vari:c:($strlen-$c+1)}
   		break
   	fi
   	flnm+=${vari:c:1}
	done

	if [ ! -e $flnm$extn ]; then
		echo $vari
		return
	fi
	
	cnt=1
	while true
	do
		if [ ! -e "$flnm($cnt)$extn" ]; then
			echo "$flnm($cnt)$extn"
			return
		fi
		let "cnt++"
	done
	
}

remove(){
	if [ $1 = "-c" ] # then recycle bin will be cleared
	then
		rm -r $recycle_bin
		remove $2
		return
	fi
	
	if [ -e $1 ]; then
		if [ ! -e $recycle_bin ] # no recycle bin directory
		then
			mkdir $recycle_bin
			echo "Recycle bin created"
		fi
		cd $recycle_bin
		loc=$(countIt $1)
		cd ..
		mv "./$1" "./$recycle_bin/$loc"
		echo "Moved to recycle bin"
	else
		echo "File not exists.."
	fi
}

read -p "Enter a filename: " file
remove $file

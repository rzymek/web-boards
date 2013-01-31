for f in front/*/*;do 
	b=back/`echo $f|cut -d'+' -f2|sed 's/_f/_b/'`; 
	id=`echo $f|cut -d'+' -f2|sed 's/_f.png//'|sed 's/-/_/g'`;
	side=`echo $f|cut -d'/' -f2`;
	if [ -f $b ];then 
		echo "${side}_$id(\"$f\",\"$b\"),"
	else 
		echo "${side}_$id(\"$f\"),"
	fi;
done #> units.txt


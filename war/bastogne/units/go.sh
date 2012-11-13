for f in front/*/*;do b=back/`echo $f|cut -d'+' -f2|sed 's/_f/_b/'`; if [ -f $b ];then echo $f=$b;else echo $f=;fi;done > units.txt


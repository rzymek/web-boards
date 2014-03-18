BEGIN{
    lx=-1
    ly=-1
    h=35
}
/^#h/ {
    split(gensub(/#h([0-9]+)_([0-9]+){(.*)}/,"\\1,\\2,\\3","g"),hex,",")
    x=hex[1]
    y=hex[2]
    val=hex[3]
    print "#h"x-1"_"(h-y)"{"val"}"
    next
}
{print}
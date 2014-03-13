ensureTransformListSize = function(svg, transformList, size) {
    for(var i=transformList.numberOfItems;i<size;i++){
        transformList.appendItem(svg.createSVGTransform());            
    }
    return transformList;    
};
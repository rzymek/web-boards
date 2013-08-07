part of webboards_client;

Map<String, String> getUriParams(String uriSearch) {
  if (uriSearch != '') {
    final List<String> paramValuePairs = uriSearch.substring(1).split('&');

    Map<String,String> paramMapping = {};    
    paramValuePairs.forEach((e) {
      if (e.contains('=')) {
        final paramValue = e.split('=');
        paramMapping[paramValue[0]] = paramValue[1];
      } else {
        paramMapping[e] = '';
      }
    });
    return paramMapping;
  }
}
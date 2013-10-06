interface xml2js {
    parseString(xml:string, options?:any, callback?:any):void;
    parseStringSync(xml:string, options?:any):any;
}
declare var xml2js:xml2js;
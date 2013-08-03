package main

import (
	"fmt"
	"github.com/moovweb/gokogiri"
	"github.com/moovweb/gokogiri/xml"
	"io/ioutil"
	"regexp"
	"strconv"
)

type xpathProcessor func(node xml.Node)

type xmldoc struct {
	*xml.XmlDocument
}

func (doc *xmldoc) do(xpath string, processor xpathProcessor) {
	nodes, err := doc.Root().Search(xpath)
	if err != nil {
		panic(err)
	}
	if len(nodes) == 0 {
		panic("No elements matching " + xpath)
	}
	for _, node := range nodes {
		processor(node)
	}
}

func main() {
	in, err := ioutil.ReadFile("../../games/bastogne/src/bastogne-orig.svg")
	if err != nil {
		panic(err)
	}
	doc, err := gokogiri.ParseXml(in)
	svg := xmldoc{doc}

	removeAll := func(node xml.Node) { node.Remove() }
	fixHexIds := func(node xml.Node) {
		id := node.Attr("id")
		rx := regexp.MustCompile(`([0-9]+)[.]([0-9]+)`)
		res := rx.FindStringSubmatch(id)
		if len(res) == 3 {
			x, _ := strconv.Atoi(res[1])
			y, _ := strconv.Atoi(res[2])
			id = fmt.Sprintf(`h%02d%02d`, x, y)
			node.SetAttr("id", id)
		}
	}
	svg.do(`//*[namespace-uri()!='http://www.w3.org/2000/svg']`, removeAll)
	svg.do(`//*/@*[namespace-uri()!='' and namespace-uri()!='http://www.w3.org/1999/xlink']`, removeAll)
	svg.do(`//*[@id='tmpl']`, func(node xml.Node) {
		node.SetAttr("style", "display:none")
	})
	svg.do(`//*[@id]`, fixHexIds)
	fmt.Println(doc)
}

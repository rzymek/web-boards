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
	nodes, _ := doc.Root().Search(xpath)
	for _, node := range nodes {
		processor(node)
	}
}

func main() {
	in, err := ioutil.ReadFile("bastogne-orig.svg")
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

	svg.do(`//*[@id='tmpl']`, removeAll)
	svg.do(`//*[@id]`, fixHexIds)
	svg.do(`//*[@id='text4105']`, func(node xml.Node) {
		fmt.Println(node)
		for k, v := range node.Attributes() {
			fmt.Println(k + ": " + v.String())
			for kk, vv := range v.Attributes() {
				fmt.Println(k + ": " + kk + " -> " + vv.String())
			}
		}
	})
	fmt.Println("----------------------")
	svg.do(`//*[@id='text4105']/@*[namespace-uri()='']`, func(node xml.Node) {
		fmt.Println(node)
	})
}

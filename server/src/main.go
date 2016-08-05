package main

import (
	"fmt"
	"encoding/json"
	"net/http"
	"net/url"
	"bytes"
//	"io"
)

type Message struct {
	URL *url.URL
	Headers string
}

func init() {
	http.HandleFunc("/", json_handler)
}


func json_handler(w http.ResponseWriter, r *http.Request) {
	m := Message{
		URL: r.URL,
	}
	var buffer bytes.Buffer
	r.Header.Write(&buffer)
	
	b, err := json.Marshal(m)
	if(err != nil) {
		fmt.Println(err)
	}else{
		w.Write(b)
	}	
}
func handler(w http.ResponseWriter, r *http.Request) {
	fmt.Fprintf(w, "Hi there: %s", )
}

func main() {
	http.ListenAndServe(":8888", nil)
}

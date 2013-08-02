package main

import (
    "fmt"
    "net/http"
)

func handler(w http.ResponseWriter, r *http.Request) {
    fmt.Fprintf(w, "Hi there, I love %s!", r.URL.Path[1:])
}

func main() {
	bind := ":8888" 
	fmt.Printf("Starting server on %s...\n", bind) 
    http.HandleFunc("/", handler)
    http.ListenAndServe(bind, nil)
}
package main

import (
    "fmt"
    "net/http"
)

func init() {
    http.HandleFunc("/", handler)
}

func handler(w http.ResponseWriter, r *http.Request) {
 	fmt.Fprintf(w, "Hi there: %s", r.URL.Path[1:])
 }

func main() {
    http.ListenAndServe(":8888", nil)
}
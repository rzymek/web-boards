package main

import (
	"appengine"
	"appengine/datastore"
	//	"bytes"
	"encoding/json"
	"fmt"
	"net/http"

//	"net/url"
)

type RunningGame struct {
	Game string
}

func init() {
	http.HandleFunc("/start", startGame)
	http.HandleFunc("/game/", gameHandler)
	http.HandleFunc("/", handler)
}

func jsonResponse(w http.ResponseWriter, v interface{}) {
	b, err := json.Marshal(v)
	if err != nil {
		fmt.Println(err)
	} else {
		w.Write(b)
	}
}

func startGame(w http.ResponseWriter, r *http.Request) {
	c := appengine.NewContext(r)

	m := RunningGame{
		Game: "bastogne",
	}
	key, err := datastore.Put(c, datastore.NewIncompleteKey(c, "runningGame", nil), &m)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	var game RunningGame
	if err = datastore.Get(c, key, &game); err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	jsonResponse(w, &game)
}

func gameHandler(w http.ResponseWriter, r *http.Request) {
	http.Redirect(w,r, "/games/bastogne/units.json", 307);
}
func handler(w http.ResponseWriter, r *http.Request) {
	fmt.Fprintf(w, "go handler: %s %s", r.URL.Path, r.URL.RawQuery)
}

func main() {
	http.ListenAndServe(":8888", nil)
}

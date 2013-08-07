package main

import (
	"testing"
	//	"encoding/json"
	"net/http"
	"net/url"

//	"fmt"
)

func TestJSON(t *testing.T) {
	resp, err := http.PostForm("http://localhost:8888/json/test",
		url.Values{"key": {"value"}})
	if err != nil {
		t.Error(err)
		return
	}
	if resp.StatusCode != 200 {
		t.Error("error status: " + resp.Status)
		return
	}
	if resp.ContentLength == 0 {
		t.Error("empty response")
		return
	}
}

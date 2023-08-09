package main

import (
	"context"
	"encoding/json"
	"net/http"
)

type ctxKey = struct{}

type User struct {
	ID    string `json:"user_id"`
	Name  string `json:"name"`
	Email string `json:"email"`
}

func GetUserFromHeader(next http.Handler) http.Handler {
	fn := func(w http.ResponseWriter, r *http.Request) {
		userStr := r.Header.Get("_user")
		var user User

		_ = json.Unmarshal([]byte(userStr), &user)

		ctx := context.WithValue(r.Context(), ctxKey{}, user)

		next.ServeHTTP(w, r.WithContext(ctx))
	}

	return http.HandlerFunc(fn)
}

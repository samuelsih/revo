package main

import (
	"encoding/json"
	"reflect"
	"strconv"
	"testing"
)

func TestByteToString(t *testing.T) {
	t.Parallel()

	str := `{"metadata": [{"name":"sdsdsdsdsds","description":"dsdsukiteyosdsdsukiteyosdsdsukiteyosdsdsukiteyosdsdsukiteyosdsdsukiteyosdsdsukiteyosdsdsukiteyosdsdsukiteyosdsd", "img":"anjay"}]}` //nolint:lll

	b, _ := json.Marshal(str)

	result := b2s(b)

	got, err := strconv.Unquote(result)
	if err != nil {
		t.Errorf("err unquote: %v", err)
	}

	if str != got {
		t.Errorf("expected %v, got %v", str, got)
	}
}

func TestRandomString(t *testing.T) {
	t.Parallel()

	result := randomStr(10)
	if result == "" {
		t.Error("result is empty")
	}
}

func TestValidate(t *testing.T) {
	t.Parallel()

	type args struct {
		index int
		input votingThemeInput
	}
	tests := []struct {
		name  string
		args  args
		want  map[string]any
		want1 bool
	}{
		{
			name: "valid input",
			args: args{
				index: 1,
				input: votingThemeInput{
					Name:        "My Voting Theme",
					Description: "This is my voting theme.",
					Image:       nil,
				},
			},
			want: map[string]any{
				"idx": 1,
			},
			want1: true,
		},
		{
			name: "name too short",
			args: args{
				index: 1,
				input: votingThemeInput{
					Name:        "My",
					Description: "This is my voting theme.",
					Image:       nil,
				},
			},
			want: map[string]any{
				"idx":  1,
				"name": "Name length must between 3 and 80 char(s)",
			},
			want1: false,
		},
		{
			name: "name too long",
			args: args{
				index: 1,
				input: votingThemeInput{
					Name:        randomStr(100),
					Description: "This is my voting theme.",
					Image:       nil,
				},
			},
			want: map[string]any{
				"idx":  1,
				"name": "Name length must between 3 and 80 char(s)",
			},
			want1: false,
		},
		{
			name: "desc too short",
			args: args{
				index: 1,
				input: votingThemeInput{
					Name:        "My Voting",
					Description: "Th",
					Image:       nil,
				},
			},
			want: map[string]any{
				"idx":         1,
				"description": "Description length must between 3 and 255 char(s)",
			},
			want1: false,
		},
		{
			name: "desc too long",
			args: args{
				index: 1,
				input: votingThemeInput{
					Name:        "My Voting",
					Description: randomStr(500),
					Image:       nil,
				},
			},
			want: map[string]any{
				"idx":         1,
				"description": "Description length must between 3 and 255 char(s)",
			},
			want1: false,
		},
	}
	for _, tt := range tests {
		tt := tt

		t.Run(tt.name, func(t *testing.T) {
			t.Parallel()
			got, got1 := validate(tt.args.index, tt.args.input)
			if !reflect.DeepEqual(got, tt.want) {
				t.Errorf("validate() got = %v, want %v", got, tt.want)
			}
			if got1 != tt.want1 {
				t.Errorf("validate() got1 = %v, want %v", got1, tt.want1)
			}
		})
	}
}

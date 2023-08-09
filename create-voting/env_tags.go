package main

import (
	"errors"
	"fmt"
	"os"
	"reflect"
	"strconv"
)

const (
	envTagName     = "env"
	defaultTagName = "default"
)

var (
	errInvalidPtrType    = errors.New("config struct must be passed by reference")
	errInvalidStructType = errors.New("config parameter must be a struct")
	errEmptyEnvTag       = errors.New("empty env tag")
)

func FillEnv(config any) error {
	v := reflect.ValueOf(config)
	if v.Kind() != reflect.Ptr {
		return errInvalidPtrType
	}

	e := v.Elem()
	if e.Kind() != reflect.Struct {
		return errInvalidStructType
	}

	t := e.Type()

	for i := 0; i < t.NumField(); i++ {
		field := t.Field(i)

		if !e.Field(i).CanSet() {
			return fmt.Errorf("%s cannot be set because private field", field.Name)
		}

		envTag, exist := field.Tag.Lookup(envTagName)
		if !exist {
			continue
		}

		defaultValue, exist := field.Tag.Lookup(defaultTagName)
		if !exist {
			defaultValue = ""
		}

		if len(envTag) == 0 && len(defaultValue) != 0 {
			return errEmptyEnvTag
		}

		envValue := os.Getenv(envTag)
		if envValue == "" {
			envValue = defaultValue
		}

		if err := setValue(envValue, e.Field(i)); err != nil {
			return err
		}
	}

	return nil
}

func setValue(value string, field reflect.Value) error {
	typ := field.Type()

	switch typ.Kind() {
	case reflect.Int, reflect.Int8, reflect.Int16, reflect.Int32, reflect.Int64:
		val, err := strconv.ParseInt(value, 0, typ.Bits())
		if err != nil {
			return err
		}

		field.SetInt(val)

	case reflect.String:
		field.SetString(value)

	case reflect.Bool:
		val, err := strconv.ParseBool(value)
		if err != nil {
			return err
		}

		field.SetBool(val)

	default:
		return errors.New("wrong type for field")
	}

	return nil
}

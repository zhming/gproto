package main

import (
	"fmt"
	"os"

	"github.com/gin-contrib/cors"
	"github.com/gin-gonic/gin"
	"github.com/go-basic/uuid"
)

func main() {
	router := gin.Default()

	router.Use(cors.Default())

	router.GET("/", func(c *gin.Context) {
		c.String(200, "Hello, Gin")
	})

	router.POST("/saveProto", func(c *gin.Context) {
		jsonData := make(map[string]string)

		c.BindJSON(&jsonData)

		protoData, status := jsonData["protoData"]

		if !status {
			panic("未获取到protoData参数")
		}

		fmt.Println(protoData)

		protoPath := "d:/temp/protobuf/"

		pb2Filename := protoPath + uuid.New() + ".proto"

		file, err := os.Create(pb2Filename)

		if nil != err {
			panic("save file fail")
		}

		file.Write([]byte(protoData))
	})

	router.Run(":6154")
}

function Chart(selector) {
    return {
        chart: d3.select(selector),
        drawChartOxis: function(config) {
            var width = config.width;
            var height = config.height;
            var arrowWidth = config.arrowWidth;
            var arrowHeight = config.arrowHeight;
            var xAxisText = config.xAxisText;
            var yAxisText = config.yAxisText;
            var leftOffset = 50;
            var topOffset = 10;

            var chart = d3.select("#chart");
            var linesData = [{
                x1: leftOffset,
                x2: leftOffset,
                y1: topOffset,
                y2: height
            }, {
                x1: leftOffset,
                x2: width,
                y1: height,
                y2: height
            }];

            var arrowsData = [{
                points: [
                    {x:leftOffset-arrowWidth, y:topOffset + arrowHeight},
                    {x:leftOffset,y:topOffset},
                    {x: leftOffset + arrowWidth, y: topOffset + arrowHeight}
                ]
            }, {
                points: [
                    {x:width - arrowHeight, y:height - arrowWidth},
                    {x:width,y:height},
                    {x: width - arrowHeight, y: height + arrowWidth}
                ]
            }
            ];

            var textData = [{
                x: leftOffset,
                y: height / 2 + 60,
                id: "yAxis",
                text: yAxisText,
                transform: "rotate(270 " + (leftOffset - 10) + " " + (height/2 + 60) + ")"
            },
                {
                    x: width / 2 - 60,
                    y: height + 18,
                    text: xAxisText,
                    id: "xAxis"
                }
            ];

            chart.selectAll("polyline")
                .data(arrowsData)
                .enter()
                .append("polyline")
                .attr("points", function(d) {
                    var value = "";
                    d.points.forEach(function(point) {
                        value += point.x + "," + point.y + " "
                    });

                    return value;
                })
                .attr("fill", "white")
                .attr("stroke", "black")
                .attr("stroke-width", 2);

            chart
                .selectAll("line")
                .data(linesData)
                .enter()
                .append("svg:line")
                .attr("stroke", "#000")
                .attr("stroke-width", 2)
                .attr("x1", function (d) {
                    return d.x1;
                })
                .attr("x2", function (d) {
                    return d.x2;
                })
                .attr("y1", function (d) {
                    return d.y1;
                })
                .attr("y2", function (d) {
                    return d.y2;
                });
            chart.selectAll("text")
                .data(textData)
                .enter()
                .append("text")
                .attr("x", function(d) {
                    return d.x;
                })
                .attr("y", function(d){
                    return d.y
                })
                .attr("transform", function (d) {
                    return d.transform;
                })
                .attr("id", function(d) {return d.id})
                .attr("font-size", 20)
                .attr("font-family", "MicrosoftSansSerif")
                .text(function(d) {return d.text});
        }
    }
}


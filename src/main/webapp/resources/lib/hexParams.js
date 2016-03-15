(function () {
    hexParams = {
        createParams: function (config) {
            var hexAngles = d3.range(Math.PI / 6, 2 * Math.PI + Math.PI / 6, Math.PI / 3);
            var hexData = config.hexData;
            var radius = config.radius;

            var svg = d3.select('#svg');
            var points = [];
            hexData[0].x = svg[0][0].clientWidth / 2;
            hexData[0].y = svg[0][0].clientHeight / 2;
            hexData[0].angle = hexAngles[0];

            var hexbin = d3.hexbin().radius(radius);

            points.push([hexData[0].x, hexData[0].y]);

            for (var i = 1; i < hexData.length; i++) {
                hexData[i].x = hexData[0].x + Math.sin(hexData[i - 1].angle) * radius * 2;
                hexData[i].y = hexData[0].y - Math.cos(hexData[i - 1].angle) * radius * 2;
                points.push([hexData[i].x, hexData[i].y]);
                hexData[i].angle = hexAngles[i % hexAngles.length];
            }

            //Draw hex
            svg.append('g')
                .selectAll('.hexagon')
                .data(hexData)
                .enter().append("path")
                .attr("class", "hexagon")
                .attr("d", function (d) {
                    return "M" + d.x + "," + d.y + hexbin.hexagon();
                })
                .attr("stroke", function (d) {
                    return d.stroke
                })
                .attr("stroke-width", "5px")
                .style("fill", "white");

            //Draw texts
            svg.selectAll("text")
                .data(hexData)
                .enter()
                .append("text")
                .attr("x", function (d) {
                    return d.x;
                })
                .attr("y", function (d) {
                    return d.y;
                })
                .attr('text-anchor', 'middle')
                .attr("font-size", 15)
                .attr("fill", "grey")
                .attr("font-family", "MicrosoftSansSerif")
                .text(function (d) {
                    return d.text
                });
        }
    }
})();
(function () {
    hexParams = function (config) {
        this.svg = d3.select('#svg');
        this.addItio
        this.filter = this.svg.append("defs")
            .append("filter")
            .attr("id", "blur")
            .append("feGaussianBlur")
            .attr("stdDeviation", 0);


        var hexAngles = d3.range(Math.PI / 6, 2 * Math.PI + Math.PI / 6, Math.PI / 3);
        var hexData = config.hexData;
        var radius = config.radius;

        this.svg.attr('filter', 'url(#blur)');
        var points = [];
        hexData[0].x = this.svg[0][0].clientWidth / 2;
        hexData[0].y = this.svg[0][0].clientHeight / 2;
        hexData[0].angle = hexAngles[0];

        var hexbin = d3.hexbin().radius(radius);

        points.push([hexData[0].x, hexData[0].y]);

        for (var i = 1; i < hexData.length; i++) {
            hexData[i].x = hexData[0].x + Math.sin(hexData[i - 1].angle) * radius * 2;
            hexData[i].y = hexData[0].y - Math.cos(hexData[i - 1].angle) * radius * 2;
            points.push([hexData[i].x, hexData[i].y]);
            hexData[i].angle = hexAngles[i % hexAngles.length];
        }

        var self = this;
        window.onParamClick = function () {
            var svg = d3.select('#svg');
            //self.filter.attr('stdDeviation', 5);
        };

        //Draw hex
        this.svg.append('g')
            .selectAll('.hexagon')
            .data(hexData)
            .enter().append("path")
            .attr("class", "hexagon")
            .attr("d", function (d) {
                return "M" + d.x + "," + d.y + hexbin.hexagon();
            })
            .attr('onclick', function (d) {
                return d.onclick;
            })
            .attr('id', function (d) {
                return d.id;
            })
            .attr("stroke-width", "5px")
            .style("fill", "white")
            .attr('stroke', 'white')
            .transition()
            .attr("stroke", function (d) {
                return d.stroke
            })
            .duration(1000);

        //Draw texts
        this.svg.selectAll("text")
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
            .attr("fill", "white")
            .attr("font-family", "MicrosoftSansSerif")
            .text(function (d) {
                return d.text;
            })
            .transition()
            .attr("fill", "grey")
            .duration(1000);
    }
})();
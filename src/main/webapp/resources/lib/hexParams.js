(function () {
    hexParams = function (config) {
        this.defaultParams = config.defaultParams;
        this.optParams = config.optParams;
        this.svg = d3.select('#svg');
        this.hexbin = d3.hexbin().radius(config.radius);
        this.filter = this.svg.append("defs")
            .append("filter")
            .attr("id", "blur")
            .append("feGaussianBlur")
            .attr("stdDeviation", 0);


        var hexAngles = d3.range(Math.PI / 6, 2 * Math.PI + Math.PI / 6, Math.PI / 3);
        var radius = config.radius;

        //this.svg.attr('filter', 'url(#blur)');

        var self = this;
        window.onParamClick = function () {
            self.filter.attr('stdDeviation', 5);
            drawHex(self.optParams);
        };


        var drawHex = function(params, filter) {
            params[0].x = self.svg[0][0].clientWidth / 2;
            params[0].y = self.svg[0][0].clientHeight / 2;
            params[0].angle = hexAngles[0];


            for (var i = 1; i < params.length; i++) {
                params[i].x = params[0].x + Math.sin(params[i - 1].angle) * radius * 2;
                params[i].y = params[0].y - Math.cos(params[i - 1].angle) * radius * 2;
                params[i].angle = hexAngles[i % hexAngles.length];
            }
            //Draw hex
            self.svg.append('g')
                .attr('filter', filter)
                .selectAll('.hexagon')
                .data(params)
                .enter().append("path")
                .attr("class", "hexagon")
                .attr("d", function (d) {
                    return "M" + d.x + "," + d.y + self.hexbin.hexagon();
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
            self.svg.append('g')
                .attr('filter', filter)
                .selectAll("text")
                .data(params)
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

        };
        drawHex(this.defaultParams, 'url(#blur)');

    }
})();
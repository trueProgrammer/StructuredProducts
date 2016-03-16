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

        this.mainGroup = this.svg.append('g')
            .attr('id', 'main')
            .attr('filter', 'url(#blur)');
        this.addGroup = this.svg.append('g')
            .attr('id', 'addParam');

        var hexAngles = d3.range(Math.PI / 6, 2 * Math.PI + Math.PI / 6, Math.PI / 3);
        var hexAngles2 = d3.range(Math.PI / 3, 2 * Math.PI + Math.PI / 3, Math.PI / 3);
        var radius = config.radius;

        var self = this;

        function onAddParamClick(data) {
            var removeIndex = self.optParams.map(function(item) { return item.id; })
                .indexOf(data.id);

            self.defaultParams.push(self.optParams.splice(removeIndex, 1)[0]);

            self.addGroup
                .selectAll("*")
                .remove();
            self.mainGroup
                .selectAll("*")
                .remove();
            self.filter.attr('stdDeviation', 0);
            prepareData(self.defaultParams);
            drawHex(self.defaultParams, self.mainGroup, onParamClick);
        }
        function onParamClick() {
            self.filter.attr('stdDeviation', 5);
            prepareDataForLinear(self.optParams);
            drawHex(self.optParams, self.addGroup, onAddParamClick);
        }

        var prepareDataForLinear = function(params) {
            params[0].x = radius + 10;
            params[0].y = radius + 10;

            for (var i = 1; i < params.length; i++) {
                if (i % 3 === 0) {
                    params[i].x = params[0].x;
                    params[i].y = params[i-1].y + radius*2 + 20;
                } else {
                    params[i].x = params[i-1].x + radius * 2;
                    params[i].y = params[i-1].y;
                }
                params[i].angle = hexAngles[i % hexAngles.length];
            }
        };

        var prepareData = function(params) {
            params[0].x = self.svg[0][0].clientWidth / 2;
            params[0].y = self.svg[0][0].clientHeight / 2;
            params[0].angle = hexAngles[0];
            var angles = hexAngles;
            var radiusPow = 2;

            for (var i = 1; i < params.length; i++) {
                if (i == 7) {
                    angles = hexAngles2;
                    radiusPow = 3.5;
                    params[i].x = params[0].x;
                    params[i].y = params[0].y - radius * 3.5;
                    params[i].angle = angles[0];
                    continue;
                }
                params[i].x = params[0].x + Math.sin(params[i - 1].angle) * radius * radiusPow;
                params[i].y = params[0].y - Math.cos(params[i - 1].angle) * radius * radiusPow;
                params[i].angle = angles[i % angles.length];
            }
        };
        var drawHex = function(params, group, onClick) {

            var innerGroups = group
                .append('g')
                .selectAll('.hexagon')
                .data(params, function(d) {return d.id})
                .enter();

            var paths = innerGroups.append("path")
                .attr("d", function (d) {
                    return "M" + d.x + "," + d.y + self.hexbin.hexagon();
                })
                .attr("stroke-width", "5px")
                .attr('stroke', 'white')
                .attr("class","hex-style")
                .transition()
                .attr("stroke", function (d) {
                    return d.stroke
                })
                .duration(1000);

            paths.each(function(path) {
                var pathDiv = d3.select(this);
                pathDiv.on('click', function(path) {
                    onClick.call(pathDiv, path);
                })
            });

            //Draw texts
            innerGroups
                .append("text")
                .attr("x", function (d) {
                    return d.x;
                })
                .attr("y", function (d) {
                    return d.y;
                })
                .attr("class","hex-style")
                .attr("class","hex-text")
                .text(function (d) {
                    return d.text;
                })
                .attr('onclick', function (d) {
                    return d.onclick;
                })
                .transition()
                .duration(1000);
            innerGroups
                .append("text")
                .attr("x", function (d) {
                    return d.x;
                })
                .attr("y", function (d) {
                    return d.y + 20;
                })
                .attr('id', function (d) {
                    return d.id;
                })
                .attr("class","hex-style")
                .attr("class","hex-text")
                .attr('onclick', function (d) {
                    return d.onclick;
                })
                .transition()
                .duration(1000);

        };
        prepareData(this.defaultParams);
        drawHex(this.defaultParams, this.mainGroup, onParamClick);

    }
})();
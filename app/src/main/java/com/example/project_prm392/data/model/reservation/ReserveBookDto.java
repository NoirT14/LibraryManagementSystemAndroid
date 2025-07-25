package com.example.project_prm392.data.model.reservation;

import com.example.project_prm392.data.WrappedList;

import java.util.List;

public class ReserveBookDto {
    private String title;
    private String coverImg;

    // Sửa tại đây:
    private WrappedList<String> authorNames;
    private WrappedList<VolumeDto> volumes;

    public String getTitle() {
        return title;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public List<String> getAuthorNames() {
        return authorNames != null ? authorNames.getValues() : null;
    }

    public List<VolumeDto> getVolumes() {
        return volumes != null ? volumes.getValues() : null;
    }

    public static class VolumeDto {
        private int volumeId;
        private int volumeNumber;
        private String volumeTitle;

        public int getVolumeId() {
            return volumeId;
        }

        public int getVolumeNumber() {
            return volumeNumber;
        }

        public String getVolumeTitle() {
            return volumeTitle;
        }

        @Override
        public String toString() {
            return volumeTitle != null ? volumeTitle : "Tập " + volumeNumber;
        }
    }
}

